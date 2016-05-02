package com.wonderpush.sdk;

import org.json.JSONObject;

public class AlertBigPictureModel extends AlertModel {

    // Modify forCurrentSettings() when adding a field below
    private CharSequence bigText;
    private CharSequence bigTitle;
    private CharSequence summaryText;
    // Modify forCurrentSettings() when adding a field above

    public AlertBigPictureModel() {
    }

    public AlertBigPictureModel(JSONObject inputJSON) {
        super(inputJSON);
    }

    @Override
    protected void fromJSONCommon(JSONObject wpAlert) {
        super.fromJSONCommon(wpAlert);
        setBigTitle(wpAlert.optString("bigTitle", null));
        setBigText(wpAlert.optString("bigText", null));
        setSummaryText(wpAlert.optString("summaryText", null));
    }

    @Override
    protected void forCurrentSettingsInternal(AlertModel _from) {
        super.forCurrentSettingsInternal(_from);
        if (_from instanceof AlertBigPictureModel) {
            AlertBigPictureModel from = (AlertBigPictureModel) _from;
            if (from.getBigText() != null) {
                setText(from.getBigText());
            }
            if (from.getBigTitle() != null) {
                setTitle(from.getBigTitle());
            }
            if (from.getSummaryText() != null) {
                setSubText(from.getSummaryText());
            }
        }
    }

    public CharSequence getBigText() {
        return bigText;
    }

    public void setBigText(CharSequence bigText) {
        this.bigText = handleHtml(bigText);
    }

    public CharSequence getBigTitle() {
        return bigTitle;
    }

    public void setBigTitle(CharSequence bigTitle) {
        this.bigTitle = handleHtml(bigTitle);
    }

    public CharSequence getSummaryText() {
        return summaryText;
    }

    public void setSummaryText(CharSequence summaryText) {
        this.summaryText = handleHtml(summaryText);
    }

}
