package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PreCollectionRemoveEvent;

public class EnversPreCollectionRemoveEventListenerImpl extends
		org.hibernate.envers.event.EnversPreCollectionRemoveEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPreCollectionRemoveEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPreRemoveCollection(final PreCollectionRemoveEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPreRemoveCollection(event);
		}
	}
}
