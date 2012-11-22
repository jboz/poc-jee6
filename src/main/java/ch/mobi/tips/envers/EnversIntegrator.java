package ch.mobi.tips.envers;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.event.EnversListenerDuplicationStrategy;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * Provides integration for Envers into Hibernate, which mainly means registering the proper event listeners.
 *
 * @author Steve Ebersole
 */
public class EnversIntegrator implements Integrator {

	public static final String AUTO_REGISTER = "hibernate.listeners.envers.autoRegister";

	@Override
	public void integrate(final Configuration configuration, final SessionFactoryImplementor sessionFactory,
			final SessionFactoryServiceRegistry serviceRegistry) {

		final EventListenerRegistry listenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);
		listenerRegistry.addDuplicationStrategy(EnversListenerDuplicationStrategy.INSTANCE);

		final AuditConfiguration enversConfiguration = AuditConfiguration.getFor(configuration);

		if (enversConfiguration.getEntCfg().hasAuditedEntities()) {
			listenerRegistry.appendListeners(EventType.POST_DELETE, new EnversPostDeleteEventListenerImpl(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_INSERT, new EnversPostInsertEventListenerImpl(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_UPDATE, new EnversPostUpdateEventListenerImpl(enversConfiguration));
			listenerRegistry.appendListeners(EventType.POST_COLLECTION_RECREATE,
					new EnversPostCollectionRecreateEventListenerImpl(enversConfiguration));
			listenerRegistry.appendListeners(EventType.PRE_COLLECTION_REMOVE, new EnversPreCollectionRemoveEventListenerImpl(
					enversConfiguration));
			listenerRegistry.appendListeners(EventType.PRE_COLLECTION_UPDATE, new EnversPreCollectionUpdateEventListenerImpl(
					enversConfiguration));
		}
	}

	@Override
	public void disintegrate(final SessionFactoryImplementor sessionFactory, final SessionFactoryServiceRegistry serviceRegistry) {
		// nothing to do afaik
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see org.hibernate.integrator.spi.Integrator#integrate(org.hibernate.metamodel.source.MetadataImplementor,
	 *      org.hibernate.engine.spi.SessionFactoryImplementor, org.hibernate.service.spi.SessionFactoryServiceRegistry)
	 */
	@Override
	public void integrate(final MetadataImplementor metadata, final SessionFactoryImplementor sessionFactory,
			final SessionFactoryServiceRegistry serviceRegistry) {
		// TODO: implement
	}
}
