/*
 * Copyright (c) 2001-2005 JGoodies Karsten Lentzsch. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *     
 *  o Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution. 
 *     
 *  o Neither the name of JGoodies Karsten Lentzsch nor the names of 
 *    its contributors may be used to endorse or promote products derived 
 *    from this software without specific prior written permission. 
 *     
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, 
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */

package andyr.jacman;

import javax.swing.LookAndFeel;

import com.jgoodies.plaf.BorderStyle;
import com.jgoodies.plaf.FontSizeHints;
import com.jgoodies.plaf.HeaderStyle;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;
import com.jgoodies.plaf.plastic.PlasticTheme;
import com.jgoodies.plaf.plastic.PlasticXPLookAndFeel;

/** 
 * Describes most of the optional settings of the JGoodies Looks.
 * Used by the <code>DemoFrame</code> to configure the UI.
 * 
 * @author  Karsten Lentzsch
 * @version $Revision: 1.1 $
 * 
 * @see     com.jgoodies.looks.BorderStyle
 * @see     com.jgoodies.looks.FontSizeHints
 * @see     com.jgoodies.looks.HeaderStyle
 * @see     com.jgoodies.looks.Options
 */
public final class Settings {
    
    private LookAndFeel selectedLookAndFeel;

    private PlasticTheme selectedTheme;

    private Boolean useSystemFonts;
    
    private FontSizeHints fontSizeHints;

    private boolean useNarrowButtons;

    private boolean tabIconsEnabled;
    
    private Boolean popupDropShadowEnabled;
    
    private String plasticTabStyle;
    
    private boolean plasticHighContrastFocusEnabled;

    private HeaderStyle menuBarHeaderStyle;

    private BorderStyle menuBarPlasticBorderStyle;

    private BorderStyle menuBarWindowsBorderStyle;

    private Boolean menuBar3DHint;
        
    private HeaderStyle toolBarHeaderStyle;

    private BorderStyle toolBarPlasticBorderStyle;

    private BorderStyle toolBarWindowsBorderStyle;

    private Boolean toolBar3DHint;
  
   
    
    // Instance Creation ******************************************************

    private Settings() {
        // Override default constructor; prevents instantiability.
    }

    public static Settings createDefault() {
        Settings settings = new Settings();
        
        //settings.setSelectedLookAndFeel(new javax.swing.plaf.metal.MetalLookAndFeel());
        settings.setSelectedLookAndFeel(new PlasticXPLookAndFeel());
        //settings.setSelectedTheme(new ExperienceBlue());
        settings.setSelectedTheme(PlasticLookAndFeel.createMyDefaultTheme());
        settings.setUseSystemFonts(Boolean.TRUE);
        settings.setFontSizeHints(FontSizeHints.MIXED);
        settings.setUseNarrowButtons(false);
        settings.setTabIconsEnabled(true);
        settings.setPlasticTabStyle(PlasticLookAndFeel.TAB_STYLE_DEFAULT_VALUE);
        settings.setPlasticHighContrastFocusEnabled(false);
        settings.setMenuBarHeaderStyle(null);
        settings.setMenuBarPlasticBorderStyle(null);
        settings.setMenuBarWindowsBorderStyle(null);
        settings.setMenuBar3DHint(Boolean.TRUE);
        settings.setToolBarHeaderStyle(null);
        settings.setToolBarPlasticBorderStyle(null);
        settings.setToolBarWindowsBorderStyle(null);
        settings.setToolBar3DHint(null);
       
        return settings;
    }


    // Accessors **************************************************************

    public FontSizeHints getFontSizeHints() {
        return fontSizeHints;
    }

    public void setFontSizeHints(FontSizeHints fontSizeHints) {
        this.fontSizeHints = fontSizeHints;
    }

    public Boolean getMenuBar3DHint() {
        return menuBar3DHint;
    }

    public void setMenuBar3DHint(Boolean menuBar3DHint) {
        this.menuBar3DHint = menuBar3DHint;
    }

    public HeaderStyle getMenuBarHeaderStyle() {
        return menuBarHeaderStyle;
    }

    public void setMenuBarHeaderStyle(HeaderStyle menuBarHeaderStyle) {
        this.menuBarHeaderStyle = menuBarHeaderStyle;
    }

    public BorderStyle getMenuBarPlasticBorderStyle() {
        return menuBarPlasticBorderStyle;
    }

    public void setMenuBarPlasticBorderStyle(BorderStyle menuBarPlasticBorderStyle) {
        this.menuBarPlasticBorderStyle = menuBarPlasticBorderStyle;
    }

    public BorderStyle getMenuBarWindowsBorderStyle() {
        return menuBarWindowsBorderStyle;
    }

    public void setMenuBarWindowsBorderStyle(BorderStyle menuBarWindowsBorderStyle) {
        this.menuBarWindowsBorderStyle = menuBarWindowsBorderStyle;
    }

    public Boolean isPopupDropShadowEnabled() {
        return popupDropShadowEnabled;
    }

    public void setPopupDropShadowEnabled(Boolean popupDropShadowEnabled) {
        this.popupDropShadowEnabled = popupDropShadowEnabled;
    }

    public boolean isPlasticHighContrastFocusEnabled() {
        return plasticHighContrastFocusEnabled;
    }

    public void setPlasticHighContrastFocusEnabled(boolean plasticHighContrastFocusEnabled) {
        this.plasticHighContrastFocusEnabled = plasticHighContrastFocusEnabled;
    }

    public String getPlasticTabStyle() {
        return plasticTabStyle;
    }

    public void setPlasticTabStyle(String plasticTabStyle) {
        this.plasticTabStyle = plasticTabStyle;
    }

    public LookAndFeel getSelectedLookAndFeel() {
        return selectedLookAndFeel;
    }

    public void setSelectedLookAndFeel(LookAndFeel selectedLookAndFeel) {
        this.selectedLookAndFeel = selectedLookAndFeel;
    }

    public PlasticTheme getSelectedTheme() {
        return selectedTheme;
    }

    public void setSelectedTheme(PlasticTheme selectedTheme) {
        this.selectedTheme = selectedTheme;
    }

    public boolean isTabIconsEnabled() {
        return tabIconsEnabled;
    }

    public void setTabIconsEnabled(boolean tabIconsEnabled) {
        this.tabIconsEnabled = tabIconsEnabled;
    }

    public Boolean getToolBar3DHint() {
        return toolBar3DHint;
    }

    public void setToolBar3DHint(Boolean toolBar3DHint) {
        this.toolBar3DHint = toolBar3DHint;
    }

    public HeaderStyle getToolBarHeaderStyle() {
        return toolBarHeaderStyle;
    }

    public void setToolBarHeaderStyle(HeaderStyle toolBarHeaderStyle) {
        this.toolBarHeaderStyle = toolBarHeaderStyle;
    }

    public BorderStyle getToolBarPlasticBorderStyle() {
        return toolBarPlasticBorderStyle;
    }

    public void setToolBarPlasticBorderStyle(BorderStyle toolBarPlasticBorderStyle) {
        this.toolBarPlasticBorderStyle = toolBarPlasticBorderStyle;
    }

    public BorderStyle getToolBarWindowsBorderStyle() {
        return toolBarWindowsBorderStyle;
    }

    public void setToolBarWindowsBorderStyle(BorderStyle toolBarWindowsBorderStyle) {
        this.toolBarWindowsBorderStyle = toolBarWindowsBorderStyle;
    }

    public boolean isUseNarrowButtons() {
        return useNarrowButtons;
    }

    public void setUseNarrowButtons(boolean useNarrowButtons) {
        this.useNarrowButtons = useNarrowButtons;
    }

    public Boolean isUseSystemFonts() {
        return useSystemFonts;
    }

    public void setUseSystemFonts(Boolean useSystemFonts) {
        this.useSystemFonts = useSystemFonts;
    }

}