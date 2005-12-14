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
    private EventList packagesEventList;
    private EventList packagesSelectedList;

    /** a widget for selecting users */
    private JList userSelect;
    
    private I18nManager i18n;

    /**
     * Create a filter list that filters the specified source list, which
     * must contain only Issue objects.
     */
    public InstallListFilter(EventList source) {
        //super(source);

        i18n = I18nManager.getI18nManager("i18n/JacmanLabels", Locale.getDefault());
        
        List<String> filterItems = new ArrayList<String>();
        filterItems.add(i18n.getString("FilterAll"));
        filterItems.add(i18n.getString("FilterNotInstalled"));
        
        // create a unique users list from the source issues list
        //usersEventList = new UniqueList(new CollectionList(source, new IssueUserator()));
        packagesEventList = new BasicEventList(filterItems);
        // create a JList that contains users
        EventListModel usersListModel = new EventListModel(packagesEventList);
        userSelect = new JList(usersListModel);

        // create an EventList containing the JList's selection
        EventSelectionModel userSelectionModel = new EventSelectionModel(packagesEventList);
        userSelect.setSelectionModel(userSelectionModel);
        packagesSelectedList = userSelectionModel.getSelected();
        userSelect.addListSelectionListener(this);

        //handleFilterChanged();
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
        //handleFilterChanged();
        Matcher newMatcher = new InstallMatcher(packagesSelectedList);
        fireChanged(newMatcher);
    }

    
}

