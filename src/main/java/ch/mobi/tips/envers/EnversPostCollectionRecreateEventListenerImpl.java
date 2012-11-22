package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PostCollectionRecreateEvent;

public class EnversPostCollectionRecreateEventListenerImpl extends
		org.hibernate.envers.event.EnversPostCollectionRecreateEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPostCollectionRecreateEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPostRecreateCollection(final PostCollectionRecreateEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPostRecreateCollection(event);
		}
	}
}
