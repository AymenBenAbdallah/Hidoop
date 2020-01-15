package ordo;

import java.util.concurrent.Semaphore;

public interface CallBack {
	public void tacheFinie();

	public Semaphore getFinTache();
}
