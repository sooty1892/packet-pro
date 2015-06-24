
public class NativeStats extends CoreThread {
	
	public NativeStats(int core) {
		super(core);
	}

	@Override
	public void run() {
		DpdkAccess.dpdk_start_stats();
	}

}
