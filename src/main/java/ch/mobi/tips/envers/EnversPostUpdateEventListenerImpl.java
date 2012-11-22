package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PostUpdateEvent;

public class EnversPostUpdateEventListenerImpl extends org.hibernate.envers.event.EnversPostUpdateEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPostUpdateEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPostUpdate(final PostUpdateEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPostUpdate(event);
		}
	}
}
