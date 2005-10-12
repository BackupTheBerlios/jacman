package andyr.jacman;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

public class PacmanPkg implements Comparable<PacmanPkg> {

	protected String name = "";
	protected String version = "";
	protected String installedVersion = "";
	protected String description = "";
	protected String repository = "";
	protected String md5sum = "";
	protected long size = -1l;
	protected List<String> depends = new ArrayList<String>();
	protected boolean installSelected = false;

	public PacmanPkg() {

	}

	public PacmanPkg(File packagePath) throws IOException {
		obtainPackageInfo(packagePath);

		setInstalledVersion("--");
	}

	protected void processInfoBuffer(String infoBuffer) {

		int pos = infoBuffer.lastIndexOf('%');

		if (pos != -1) {
			String header = infoBuffer.substring(0, pos + 1);
			String info = infoBuffer.substring(pos + 1);

			if (header.startsWith("%NAME%")) {
				this.setName(info);
			} else if (header.startsWith("%DESC%")) {
				this.setDescription(info);
			} else if (header.startsWith("%VERSION%")) {
				this.setVersion(info);
			} else if (header.startsWith("%CSIZE%")) {
				this.setSize(Long.parseLong(info));
			} else if (header.startsWith("%MD5SUM%")) {
				this.setMd5sum(info);
			}
		}

	}

	protected void processDependsBuffer(String depBuffer) {
		int pos = depBuffer.lastIndexOf('%');

		if (pos != -1) {
			String header = depBuffer.substring(0, pos + 1);
			String info = depBuffer.substring(pos + 1);

			if (header.startsWith("%DEPENDS%")) {
				StringTokenizer tokens = new StringTokenizer(info);

				while (tokens.hasMoreTokens()) {
					this.addDepends(tokens.nextToken());
				}
			}

		}
	}

	public void obtainPackageInfo(File packagePath) throws IOException {

		// The packagePath is a dir containing details about a package in that
		// repo.

		// Parse the 'desc' file contain information about the package.
		File descPath = new File(packagePath.getPath() + "/desc");

		BufferedReader reader = new BufferedReader(new FileReader(descPath));
		String buffer = "";
		String infoBuffer = "";

		while ((buffer = reader.readLine()) != null) {
			buffer = buffer.trim();

			if (buffer.startsWith("%")) {
				processInfoBuffer(infoBuffer);
				infoBuffer = buffer;
			} else if (!buffer.equals("")) {
				infoBuffer += buffer;
			}

		}
		processInfoBuffer(infoBuffer);
		reader.close();

		// Parse the 'depends' file to obtain the packages that this package
		// depends on.

		File dependsPath = new File(packagePath.getPath() + "/depends");

		reader = new BufferedReader(new FileReader(dependsPath));
		buffer = "";
		infoBuffer = "";

		while ((buffer = reader.readLine()) != null) {
			buffer = buffer.trim();

			if (buffer.startsWith("%")) {
				processDependsBuffer(infoBuffer);
				infoBuffer = buffer;
			} else if (!buffer.equals("")) {
				infoBuffer += " " + buffer;
			}

		}
		processDependsBuffer(infoBuffer);
		reader.close();

		// The repository can be obtained from the path itself.

		setRepository(packagePath.getParentFile().getName());

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getInstalledVersion() {
		return installedVersion;
	}

	public void setInstalledVersion(String installedVersion) {
		this.installedVersion = installedVersion;
	}

	public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean getInstallSelected() {
		return installSelected;
	}

	public void setInstallSelected(boolean selected) {
		this.installSelected = selected;
	}

	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public List<String> getDepends() {
		return depends;
	}

	public void setDepends(List<String> depends) {
		this.depends = depends;
	}

	public void addDepends(String newDepend) {

		depends.add(newDepend);
	}

	@Override
	public String toString() {

		StringBuilder output = new StringBuilder();

		output.append("Package name: " + getName() + '\n');
		output.append("Package desc: " + getDescription() + '\n');
		output.append("Package repository: " + getRepository() + '\n');
		output.append("Package size: " + getSize() + '\n');
		output.append("Package md5sum: " + getMd5sum() + '\n');
		output.append("Package depends on: ");

		for (int i = 0; i < depends.size(); i++) {
			output.append(depends.get(i) + " ");

		}
		output.append('\n');

		return output.toString();
	}

	public int compareTo(PacmanPkg p) {

		if (getName().equals(p.getName()))
			return getVersion().compareTo(p.getVersion());
		return getName().compareTo(p.getName());
	}

}
