package ch.mobi.tips.envers;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.event.spi.PostInsertEvent;

public class EnversPostInsertEventListenerImpl extends org.hibernate.envers.event.EnversPostInsertEventListenerImpl {
	private static final long serialVersionUID = 1L;

	public EnversPostInsertEventListenerImpl(final AuditConfiguration enversConfiguration) {
		super(enversConfiguration);
	}

	@Override
	public void onPostInsert(final PostInsertEvent event) {
		if (HistoricManager.getInstance().isHistorization()) {
			super.onPostInsert(event);
		}
	}
}
