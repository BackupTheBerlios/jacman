/*
 * Created on Jun 21, 2005
 *
 */
package andyr.jacman;



import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ca.odell.glazedlists.AbstractFilterList;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.CollectionList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

public class InstallListFilter extends AbstractFilterList implements ListSelectionListener {

    /** a list of users */
    private EventList usersEventList;
    private EventList usersSelectedList;

    /** a widget for selecting users */
    private JList userSelect;

    /**
     * Create a filter list that filters the specified source list, which
     * must contain only Issue objects.
     */
    public InstallListFilter(EventList source) {
        super(source);

        
        List filterItems = new ArrayList();
        filterItems.add("All");
        filterItems.add("Not installed");
        
        // create a unique users list from the source issues list
        //usersEventList = new UniqueList(new CollectionList(source, new IssueUserator()));
        usersEventList = new BasicEventList(filterItems);
        // create a JList that contains users
        EventListModel usersListModel = new EventListModel(usersEventList);
        userSelect = new JList(usersListModel);

        // create an EventList containing the JList's selection
        EventSelectionModel userSelectionModel = new EventSelectionModel(usersEventList);
        userSelect.setSelectionModel(userSelectionModel);
        usersSelectedList = userSelectionModel.getSelected();
        userSelect.addListSelectionListener(this);

        handleFilterChanged();
    }

    /**
     * Get the widget for selecting users.
     */
    public JList getUserSelect() {
        userSelect.setSelectedIndex(0);
        return userSelect;
    }

    /**
     * When the JList selection changes, refilter.
     */
    public void valueChanged(ListSelectionEvent e) {
        handleFilterChanged();
    }

    /**
     * Test whether to include or not include the specified issue based
     * on whether or not their user is selected.
     */
    public boolean filterMatches(Object o) {
        if (o == null) return false;
        if (usersSelectedList.isEmpty()) return true;
        PacmanPkg pkg = (PacmanPkg)o;
        
        if (usersSelectedList.contains("All")) {
            return true;
        }
        else if (usersSelectedList.contains("Not installed")) {
            if (pkg.getInstalledVersion().equals("--")) {
                //System.out.println(pkg.getName() + " not installed");
                return true;
            }
        }
        
        return false;
        
        // see if the two lists have just one intersection
        /*List users = issue.getAllUsers();
        for(Iterator u = users.iterator(); u.hasNext(); ) {
            String user = (String)u.next();
            if(usersSelectedList.contains(user)) return true;
        }*/

        // no intersection
        //return false;
    }
}

