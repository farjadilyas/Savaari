package com.farjad.savaari.settings;

public class UserSettings {

    private boolean syncTheme;

    private int themeVar;
    private boolean autoDarkTheme;

    UserSettings() { Initialize(false, 1, false); }

    public void Initialize(boolean syncTheme, int themeVar, boolean autoDarkTheme) {
        setSyncTheme(syncTheme);
        setThemeVar(themeVar);
        setAutoDarkTheme(autoDarkTheme);
    }

    public int getThemeVar() {
        return themeVar;
    }

    public void setThemeVar(int themeVar) {
        this.themeVar = themeVar;
    }
    public boolean isSyncTheme() {
        return syncTheme;
    }

    public void setSyncTheme(boolean syncTheme) {
        this.syncTheme = syncTheme;
    }

    public boolean isAutoDarkTheme() {
        return autoDarkTheme;
    }

    public void setAutoDarkTheme(boolean autoDarkTheme) {
        this.autoDarkTheme = autoDarkTheme;
    }

}
