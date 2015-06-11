import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/*
 * Output stream to redirect printlns to 2 streams
 * Generally used for streams to console and gui
 */

public class MultiStream extends OutputStream {
	
	private List<OutputStream> streams;
	
	public MultiStream() {
		streams = new ArrayList<OutputStream>();
	}
	
	public MultiStream(OutputStream os) {
		streams = new ArrayList<OutputStream>();
		streams.add(os);
	}
	
	public MultiStream(OutputStream os1, OutputStream os2) {
		streams = new ArrayList<OutputStream>();
		streams.add(os1);
		streams.add(os2);
	}
	
	public void addStream(OutputStream os) {
		streams.add(os);
	}

	@Override
	public void write(int b) {
		for (OutputStream os : streams) {
			try {
				os.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
