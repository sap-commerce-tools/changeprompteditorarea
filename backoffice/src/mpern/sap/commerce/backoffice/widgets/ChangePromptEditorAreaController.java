package mpern.sap.commerce.backoffice.widgets;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import org.zkoss.zul.Messagebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChangePromptEditorAreaController extends DefaultEditorAreaController {
    private static final long serialVersionUID = 1L;
    public static final String MODIFIED_POPUP_OPEN = "modified-popup-open";
    public static final String LAST_SEEN_INPUT_OBJECT = "lastSeenInputObject";


    @SocketEvent(
            socketId = "inputObject"
    )
    @Override
    public void setObject(Object inputObject) {
        setValue(LAST_SEEN_INPUT_OBJECT, inputObject);
        if (Boolean.TRUE.equals(getValue(MODIFIED_POPUP_OPEN, Boolean.class))) {
            return;
        }
        if (super.isModelValueChanged()) {
            displayWarningMessage();
        } else {
            super.setObject(inputObject);
        }
    }

    private void displayWarningMessage() {
        Map<String, String> args = new HashMap<>();
        List<Messagebox.Button> buttonList = new ArrayList<>();
        List<String> labelList = new ArrayList<>();
        ValidationResult validation = super.doValidate(getCurrentObject(), true);
        if (validation.getHighestSeverity().isHigherThan(ValidationSeverity.WARN)) {
            args.put("saveDisabled", getLabel("modified.save.disabled"));
        } else {
            args.put("width", "500");

            buttonList.add(Messagebox.Button.YES);
            labelList.add(getLabel("modified.save-continue"));
        }
        buttonList.add(Messagebox.Button.IGNORE);
        labelList.add(getLabel("modified.discard-continue"));

        buttonList.add(Messagebox.Button.CANCEL);
        labelList.add(getLabel("modified.cancel"));

        String templateBefore = Messagebox.getTemplate();
        try {
            Messagebox.setTemplate("/cng/notifyingeditorarea/changedmessagebox.zul");
            setValue(MODIFIED_POPUP_OPEN, Boolean.TRUE);
            Messagebox.show(
                    getLabel("modified.message"),
                    getLabel("modified.title"),
                    buttonList.toArray(new Messagebox.Button[0]),
                    labelList.toArray(new String[0]),
                    Messagebox.EXCLAMATION,
                    Messagebox.Button.YES,
                    event -> {
                        getWidgetInstanceManager().getModel().remove(MODIFIED_POPUP_OPEN);
                        switch (event.getButton()) {
                            case YES:
                                ChangePromptEditorAreaController.super.saveObjectInternal();
                            case IGNORE: // fall through
                                ChangePromptEditorAreaController.super.setObject(getValue(LAST_SEEN_INPUT_OBJECT, Object.class));
                            case CANCEL:
                                break;
                            default:
                                break;
                        }
                    },
                    args
            );
        } finally {
            Messagebox.setTemplate(templateBefore);
        }
    }
}
