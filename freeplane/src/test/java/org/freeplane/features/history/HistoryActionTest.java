package org.freeplane.features.history;

import org.freeplane.features.help.HistoryAction;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

public class HistoryActionTest {

    @Test
    public void checkSetButtonUndo() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        JButton button = HistoryAction.createButton("testButton", gbl, gbc, 0);
        HistoryAction.setButtonUndo(button);

        assertFalse(button.isContentAreaFilled());
    }
}
