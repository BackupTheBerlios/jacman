package andyr.jacman;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * User: andyr
 * Date: May 23, 2005
 * Time: 10:07:12 AM
 *
 */
public class PacmanConf {

    private File confPath;
    private List<String> repositories = new ArrayList<String>();
    private List<String> ignorePackages = new ArrayList<String>();
    private File dbPath = new File("/var/lib/pacman");
    private File cachePath = new File("/var/cache/pacman/pkg");
    private Boolean iLoveCandy = false;

    public PacmanConf(String confPath) throws FileNotFoundException, IOException {
        this(new File(confPath));
    }

    public PacmanConf(File confPath) throws FileNotFoundException, IOException {

        this.confPath = confPath;
        parseConf();


    }

    private void parseConf() throws FileNotFoundException, IOException {

        BufferedReader reader = new BufferedReader(new FileReader(confPath));

        String buffer = "";

        while ((buffer = reader.readLine()) != null) {
            buffer = buffer.trim();
            if (buffer.startsWith("IgnorePkg")) {
                processIgnorePkgLine(buffer);
            }
            else if (buffer.startsWith("DBPath")) {
                processDBPathLine(buffer);
            }
            else if (buffer.startsWith("ILoveCandy")) {
                setILoveCandy(true);
            }
            else if (buffer.startsWith("[")) {
                if (!buffer.startsWith("[options]")) {
                    processRepoLine(buffer);
                }

            }

        }

        reader.close();
    }

    private void processRepoLine(String buffer) {

        int openPos = buffer.indexOf('[');
        int closePos = buffer.indexOf(']');

        if (openPos != -1 && closePos != -1) {
            String repoName = buffer.substring(openPos+1, closePos);
            repositories.add(repoName);
        }
    }

    private void processDBPathLine(String dbLine) {

        int equalsPos = dbLine.indexOf('=');
        if (equalsPos != -1) {
            String path = dbLine.substring(equalsPos).trim();

            dbPath = new File(path);

        }
    }

    private void processIgnorePkgLine(String ignoreLine) {

        // Get the items after the equals (=) sign

        int equalsPos = ignoreLine.indexOf('=');
        if (equalsPos != -1) {
            String entries = ignoreLine.substring(equalsPos+1).trim();

            StringTokenizer tokens = new StringTokenizer(entries);

            while (tokens.hasMoreTokens()) {
                ignorePackages.add(tokens.nextToken());
            }
        }
    }

    public List getRepositories() {
        return repositories;
    }

    public List getIgnorePackages() {
        return ignorePackages;
    }

    public File getDbPath() {
        return dbPath;
    }

    public File getConfPath() {
        return confPath;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append("pacman.conf path: " + getConfPath().getPath() + '\n');
        output.append("pacman DB path: " + getDbPath().getPath() + '\n');

        output.append("Repositories: ");
        for (int i = 0; i < repositories.size(); i++) {
            output.append(repositories.get(i) + " ");
        }
        output.append("\n");

        output.append("IgnorePkgs: ");
        for (int i = 0; i < ignorePackages.size(); i++) {
            output.append(ignorePackages.get(i) + " ");
        }
        output.append("\n");

        return output.toString();
    }

    public File getCachePath() {
        return cachePath;
    }

    public void setCachePath(File cachePath) {
        this.cachePath = cachePath;
    }

    public Boolean getILoveCandy() {
        return iLoveCandy;
    }

    public void setILoveCandy(Boolean loveCandy) {
        iLoveCandy = loveCandy;
    }
}
