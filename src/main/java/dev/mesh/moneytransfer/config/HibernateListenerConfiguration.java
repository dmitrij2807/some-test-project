package dev.mesh.moneytransfer.config;

import dev.mesh.moneytransfer.domain.listener.RootAwareFlushEventListener;
import dev.mesh.moneytransfer.domain.listener.RootAwareInsertEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.stereotype.Component;

@Component
public class HibernateListenerConfiguration {

  @PersistenceUnit
  private EntityManagerFactory emf;

  private final RootAwareFlushEventListener rootAwareFlushEventListener;
  private final RootAwareInsertEventListener rootAwareInsertEventListener;

  public HibernateListenerConfiguration(RootAwareFlushEventListener rootAwareFlushEventListener,
      RootAwareInsertEventListener rootAwareInsertEventListener) {
    this.rootAwareFlushEventListener = rootAwareFlushEventListener;
    this.rootAwareInsertEventListener = rootAwareInsertEventListener;
  }

  @PostConstruct
  protected void init() {
    SessionFactoryImpl sessionFactory = emf.unwrap(SessionFactoryImpl.class);
    EventListenerRegistry registry = sessionFactory.getServiceRegistry().getService(EventListenerRegistry.class);
    assert registry != null;
    registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(rootAwareInsertEventListener);
    registry.getEventListenerGroup(EventType.FLUSH_ENTITY).appendListener(rootAwareFlushEventListener);
  }
}
