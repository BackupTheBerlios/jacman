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

import java.util.Comparator;

/**
 * Created by IntelliJ IDEA.
 * User: andyr
 * Date: May 23, 2005
 * Time: 4:21:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class PackageNameComparitor implements Comparator {
    public int compare(Object o1, Object o2) {

        String p1 = ((PacmanPkg)o1).getName();
        String p2 = ((PacmanPkg)o2).getName();


        return p1.compareTo(p2);
    }
}
