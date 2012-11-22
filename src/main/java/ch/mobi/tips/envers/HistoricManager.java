package ch.mobi.tips.envers;

/**
 * Flag to activate hibernate envers historization.
 *
 * @author Julien Boz
 */
public class HistoricManager {

	private static final HistoricManager instance = new HistoricManager();

	private HistoricManager() {
	}

	public static HistoricManager getInstance() {
		return instance;
	}

	private boolean historization = false;

	public boolean isHistorization() {
		return historization;
	}

	public void toggleHistorization() {
		historization = !historization;
	}
}
