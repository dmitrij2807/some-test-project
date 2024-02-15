package dev.mesh.moneytransfer.domain.listener;

import dev.mesh.moneytransfer.domain.model.RootAware;
import org.hibernate.LockMode;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.springframework.stereotype.Component;

@Component
public class RootAwareInsertEventListener implements PreInsertEventListener {

  @Override
  public boolean onPreInsert(PreInsertEvent event) {
    final Object entity = event.getEntity();

    if (entity instanceof RootAware) {
      RootAware rootAware = (RootAware) entity;
      Object root = rootAware.root();
      event.getSession().lock(root, LockMode.OPTIMISTIC_FORCE_INCREMENT);
    }
    return false;
  }
}
