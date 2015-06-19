
public class NativeStats extends CoreThread {

	@Override
	public void run() {
		DpdkAccess.dpdk_start_stats();
	}

}
