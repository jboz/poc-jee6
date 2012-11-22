package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PostDeleteEvent;

public class EnversPostDeleteEventListenerImpl extends org.hibernate.envers.event.EnversPostDeleteEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPostDeleteEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPostDelete(final PostDeleteEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPostDelete(event);
		}
	}
}
