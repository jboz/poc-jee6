package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PreCollectionUpdateEvent;

public class EnversPreCollectionUpdateEventListenerImpl extends
		org.hibernate.envers.event.EnversPreCollectionUpdateEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPreCollectionUpdateEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPreUpdateCollection(final PreCollectionUpdateEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPreUpdateCollection(event);
		}
	}
}
