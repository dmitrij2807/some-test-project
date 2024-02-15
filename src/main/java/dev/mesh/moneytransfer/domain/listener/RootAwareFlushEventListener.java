package dev.mesh.moneytransfer.domain.listener;

import dev.mesh.moneytransfer.domain.model.RootAware;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.spi.FlushEntityEvent;
import org.hibernate.event.spi.FlushEntityEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.springframework.stereotype.Component;

@Component
public class RootAwareFlushEventListener implements FlushEntityEventListener {

  @Override
  public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
    final EntityEntry entry = event.getEntityEntry();
    final Object entity = event.getEntity();
    final boolean mightBeDirty = entry.requiresDirtyCheck(entity);

    if (mightBeDirty && entity instanceof RootAware) {
      RootAware rootAware = (RootAware) entity;
      if (updated(event) || deleted(event)) {
        Object root = rootAware.root();
        incrementRootVersion(event, root);
      }
    }
  }

  private void incrementRootVersion(FlushEntityEvent event, Object root) {
    event.getSession().lock(root, LockMode.OPTIMISTIC_FORCE_INCREMENT);
  }

  private boolean deleted(FlushEntityEvent event) {
    return event.getEntityEntry().getStatus() == Status.DELETED;
  }

  private boolean updated(FlushEntityEvent event) {
    int[] properties;
    final EntityEntry entry = event.getEntityEntry();
    final Object entity = event.getEntity();
    final Object[] values = event.getPropertyValues();

    EntityPersister persister = entry.getPersister();
    SessionImplementor session = event.getSession();

    if (event.hasDatabaseSnapshot()) {
      properties = persister.findModified(
          event.getDatabaseSnapshot(), values, entity, session
      );
    } else {
      properties = persister.findDirty(
          values, entry.getLoadedState(), entity, session
      );
    }

    return properties != null;
  }

}
