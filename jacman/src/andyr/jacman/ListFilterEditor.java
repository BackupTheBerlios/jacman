/*
 * Created on Dec 4, 2006
 *
 */
package andyr.jacman;

import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.swing.JList;
import javax.swing.ListSelectionModel;

import andyr.jacman.utils.I18nManager;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.UniqueList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

public class ListFilterEditor extends AbstractMatcherEditor<PacmanPkg> {
    
    /** a list that maintains selection */
    private EventList<String> selectedFilters = null;
    
    /** a list of all the possible filters **/
    //private UniqueList<String> allFilters = null;
    private EventList<String> allFilters = null;
    
    /** a widget for selecting users */
    private JList filterSelect;
    
    private static I18nManager i18n;
    
    public ListFilterEditor() {
        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        BasicEventList<String> packagesEventList = new BasicEventList<String>();
        packagesEventList.add(i18n.getString("FilterAll"));
        packagesEventList.add(i18n.getString("FilterNotInstalled"));
        packagesEventList.add(i18n.getString("FilterOutOfDate"));
        packagesEventList.add(i18n.getString("FilterUpToDate"));
        
        for (String repo: Jacman.pacmanConf.getRepositories()) {
            packagesEventList.add(i18n.getString("FilterRepoPrefix") + repo);
        }
        
        //allFilters = new UniqueList<String>(packagesEventList);
        allFilters = packagesEventList;
        
        // create a JList that contains filters
        final EventListModel<String> filtersListModel = new EventListModel<String>(allFilters);
        filterSelect = new JList(filtersListModel);
        
        // create an EventList containing the JList's selection
        final EventSelectionModel<String> filterSelectionModel = new EventSelectionModel<String>(allFilters);
        filterSelect.setSelectionModel(filterSelectionModel);
        setSelectionList(filterSelectionModel.getSelected());
        
    }
    
    /**
     * Get the widget for selecting users.
     */
    public JList getFilterSelect() {
        filterSelect.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        filterSelect.setSelectedIndex(0);
        return filterSelect;
    }
    
    /**
     * Sets the selection driven EventList which triggers filter changes.
     */
    public void setSelectionList(EventList<String> filtersSelectedList) {
        this.selectedFilters = filtersSelectedList;
        filtersSelectedList.addListEventListener(new SelectionChangeEventList());
    }
    
    /**
     * Allow access to the list of filters
     */
    public EventList<String> getFiltersList() {
        return allFilters;
    }
    
    /**
     * An EventList to respond to changes in selection from the ListEventViewer.
     */
    private final class SelectionChangeEventList implements ListEventListener<String> {

        public void listChanged(ListEvent<String> listChanges) {
//          if we have all or no filters selected, match all objects
            if(selectedFilters.isEmpty() || selectedFilters.size() == allFilters.size()) {
                fireMatchAll();
                return;
            }
            
            // match the selected subset of users
            final FilterMatcher newFilterMatcher = new FilterMatcher(selectedFilters);

            // get the previous matcher. If it wasn't a user matcher, it must
            // have been an 'everything' matcher, so the new matcher must be
            // a constrainment of that
            final Matcher<PacmanPkg> previousMatcher = getMatcher();
            if(!(previousMatcher instanceof FilterMatcher)) {
                fireConstrained(newFilterMatcher);
                return;
            }
            final FilterMatcher previousFilterMatcher = (FilterMatcher)previousMatcher;

            // Figure out what type of change to fire. This is an optimization over
            // always calling fireChanged() because it allows the FilterList to skip
            // extra elements by knowing how the new matcher relates to its predecessor
            boolean relaxed = newFilterMatcher.isRelaxationOf(previousMatcher);
            boolean constrained = previousFilterMatcher.isRelaxationOf(newFilterMatcher);
            if(relaxed && constrained) return;

            if(relaxed) fireRelaxed(newFilterMatcher);
            else if(constrained) fireConstrained(newFilterMatcher);
            else fireChanged(newFilterMatcher);
            
        }
    
    }
    
    private static class FilterMatcher implements Matcher<PacmanPkg> {

        private final Set<String> filters;
        
        /**
         * Create a new {@link FilterMatcher}, creating a private copy
         * of the specified {@link Collection} to match against. A private
         * copy is made because {@link Matcher}s must be immutable.
         */
        public FilterMatcher(Collection<String> filters) {
            this.filters = new HashSet<String>(filters);
        }
        
        /**
         * @return true if this matches every {@link Issue} the other matches.
         */
        public boolean isRelaxationOf(Matcher other) {
            if(!(other instanceof FilterMatcher)) return false;
            FilterMatcher otherUserMatcher = (FilterMatcher)other;
            return filters.containsAll(otherUserMatcher.filters);
        }
        
        public boolean matches(PacmanPkg pacmanPkg) {
            if (pacmanPkg == null) return false;
            if (filters.isEmpty()) return true;
            
            
            
            if (filters.contains(i18n.getString("FilterAll"))) {
                return true;
            }
            else if (filters.contains(i18n.getString("FilterNotInstalled"))) {
                if (pacmanPkg.getInstalledVersion().equals("--")) {
                    return true;
                }
            }
            else if (filters.contains(i18n.getString("FilterUpToDate"))) {
                if (!pacmanPkg.getInstalledVersion().equals("--")) {
                    return pacmanPkg.getVersion().equals(pacmanPkg.getInstalledVersion());
                }
                
            }
            else if (filters.contains(i18n.getString("FilterOutOfDate"))) {
                if (!pacmanPkg.getInstalledVersion().equals("--")) {
                    return !pacmanPkg.getVersion().equals(pacmanPkg.getInstalledVersion());
                }
                
            }
            
            for (String repo: Jacman.pacmanConf.getRepositories()) {
                if (filters.contains(i18n.getString("FilterRepoPrefix") + repo)) {
                    return pacmanPkg.getRepository().equals(repo);
                }
            }
            
            return false;
            
        }
    
    }

}
