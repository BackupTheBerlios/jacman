package andyr.jacman;

import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: andyr
 * Date: May 23, 2005
 * Time: 1:26:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class InstalledPacmanPkg extends PacmanPkg {

    protected String licence = "";
    protected String groups = "";
    protected String url = "";
    protected String buildDate = "";
    protected String installDate = "";
    protected String packager = "";
    protected String reason = "";
    protected String arch = "";

    public InstalledPacmanPkg() {
        super();
    }

    public InstalledPacmanPkg(File packagePath) throws IOException {
        super(packagePath);
    }
    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(String buildDate) {
        this.buildDate = buildDate;
    }

    public String getInstallDate() {
        return installDate;
    }

    public void setInstallDate(String installDate) {
        this.installDate = installDate;
    }

    public String getPackager() {
        return packager;
    }

    public void setPackager(String packager) {
        this.packager = packager;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }

    @Override
    protected void processInfoBuffer(String infoBuffer) {

        int pos = infoBuffer.lastIndexOf('%');

        if (pos != -1) {
            String header = infoBuffer.substring(0, pos+1);
            String info = infoBuffer.substring(pos+1);

            //System.out.println(header);
            //System.out.println(info);

            if (header.startsWith("%NAME%")) {
                this.setName(info);
            }
            else if (header.startsWith("%DESC%")) {
                this.setDescription(info);
            }
            else if (header.startsWith("%VERSION%")) {
                this.setVersion(info);
            }
            else if (header.startsWith("%SIZE%")) {
                this.setSize(Long.parseLong(info));
            }
            else if (header.startsWith("%MD5SUM%")) {
                this.setMd5sum(info);
            }
            else if (header.startsWith("%URL%")) {
                this.setUrl(info);
            }
            else if (header.startsWith("%LICENCE%")) {
                this.setLicence(info);
            }
            else if (header.startsWith("%GROUPS")) {
                this.setGroups(info);
            }
            else if (header.startsWith("%BUILDDATE%")) {
                this.setBuildDate(info);
            }
            else if (header.startsWith("%INSTALLDATE%")) {
                this.setInstallDate(info);
            }
            else if (header.startsWith("%PACKAGER%")) {
                this.setPackager(info);
            }
            else if (header.startsWith("%ARCH%")) {
                this.setArch(info);
            }
            else if (header.startsWith("%REASON%")) {
                this.setReason(info);
            }
        }



    }

    @Override
    public String toString() {

        StringBuilder output = new StringBuilder();

        output.append("Package name: " + getName() + '\n');
        output.append("Package desc: " + getDescription() + '\n');
        output.append("Package repository: " + getRepository() + '\n');
        output.append("Package size: " + getSize() + '\n');
        output.append("Package md5sum: " + getMd5sum() + '\n');
        output.append("Package licence: " + getLicence() + '\n');
        output.append("Package groups: " + getGroups() + '\n');
        output.append("Package arch: " + getArch() + '\n');
        output.append("Package packager: " + getPackager() + '\n');
        output.append("Package url: " + getUrl() + '\n');
        output.append("Package build date: " + getBuildDate() + '\n');
        output.append("Package install date: " + getInstallDate() + '\n');
        output.append("Package reason: " + getReason() + '\n');
        output.append("Package depends on: ");

        for (int i = 0; i < depends.size(); i++) {
            output.append(depends.get(i) + " ");

        }
        output.append('\n');

        return output.toString();
    }
}
