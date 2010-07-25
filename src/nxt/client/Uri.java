package nxt.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class Uri {

	@SuppressWarnings("unused")
	private final static Logger logger = Logger.getLogger(Uri.class.getName());

	private NXTComm nxtComm;
	private UriInfo uriInfo;

	public Uri(String uri) {
		uriInfo = new UriInfo(uri);
	}

	public OutputStream getOutputStream() throws IOException {
		return nxtComm.getOutputStream();
	}

	public InputStream getInputStream() throws IOException {
		return nxtComm.getInputStream();
	}
	
	public Connection open() throws IOException{
		Connection connection = null;
		if ("usb".equals(uriInfo.getProtocol())) {
			try {
				// se la connessione è giù, l'attivo
				if (nxtComm == null){
					nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.USB);
					NXTInfo[] info = nxtComm.search(null, NXTCommFactory.USB);
					nxtComm.open(info[0]);
				} else {
					throw new IOException("connection already open");
				}
				// preparo una nuova connessione
				connection = new Connection(getInputStream(), getOutputStream());
			} catch (NXTCommException e) {
				throw new IOException("Cannot connect to device using usb");
			}
		} else {
			throw new IOException("Unknown protocol " + uriInfo.getProtocol());
		}
		return connection;
	}
	
	public void close() throws IOException{
		nxtComm.getOutputStream().close();
		nxtComm.getInputStream().close();
		// nxtComm.close();
		nxtComm = null;
	}
	
	public boolean isOpen(){
		return (nxtComm != null);
	}

	public UriInfo getUriInfo() {
		return uriInfo;
	}

	public void setUriInfo(UriInfo uriInfo) {
		this.uriInfo = uriInfo;
	}

}
