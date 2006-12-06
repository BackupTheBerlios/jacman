/*
 * 
 * Copyright 2005 The Jacman Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package andyr.jacman;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import andyr.jacman.utils.I18nManager;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.matchers.AbstractMatcherEditor;
import ca.odell.glazedlists.matchers.Matcher;
import ca.odell.glazedlists.swing.EventListModel;
import ca.odell.glazedlists.swing.EventSelectionModel;

public class InstallListFilter extends AbstractMatcherEditor implements ListSelectionListener {

    /** a list of users */
    private EventList<String> packagesEventList;
    private EventList packagesSelectedList;

    private EventSelectionModel<String> userSelectionModel;
    
    /** a widget for selecting users */
    private JList filterSelect;
    
    private I18nManager i18n;

    /**
     * Create a filter list that filters the specified source list, which
     * must contain only Issue objects.
     */
    public InstallListFilter() {
        //super(source);

        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        
        //List<String> filterItems = new ArrayList<String>();
        //filterItems.add(i18n.getString("FilterAll"));
        //filterItems.add(i18n.getString("FilterNotInstalled"));
        
        
        packagesEventList = new BasicEventList<String>();
        packagesEventList.add(i18n.getString("FilterAll"));
        packagesEventList.add(i18n.getString("FilterNotInstalled"));
        
        // create a JList that contains users
        EventListModel<String> usersListModel = new EventListModel<String>(packagesEventList);
        filterSelect = new JList(usersListModel);

        // create an EventList containing the JList's selection
        userSelectionModel = new EventSelectionModel<String>(packagesEventList);
        filterSelect.setSelectionModel(userSelectionModel);
        
        filterSelect.addListSelectionListener(this);

        //handleFilterChanged();
    }
    
    

    /**
     * Get the widget for selecting filter contraints.
     */
    public JList getFilterSelect() {
        //userSelect.setSelectedIndex(0);
        return filterSelect;
    }

    /**
     * When the JList selection changes, refilter.
     */
    public void valueChanged(ListSelectionEvent e) {
        packagesSelectedList = userSelectionModel.getSelected();
        Matcher newMatcher = new InstallMatcher(packagesSelectedList);
        fireChanged(newMatcher);
    }

    
}

