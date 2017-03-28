/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2008 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitry Polivaev
 *
 *  This file is modified by Dimitry Polivaev in 2008.
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.freeplane.features.help;

import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.undo.CompoundActor;
import org.freeplane.core.undo.IUndoHandler;
import org.freeplane.core.undo.UndoHandler;
import org.freeplane.core.util.TextUtils;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.ui.IMapViewChangeListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ListIterator;


public class HistoryAction extends AFreeplaneAction {
    private static final long serialVersionUID = 1L;
    private static JDialog dialog;
    private static JPanel panel;
    private static JScrollPane scrollPane;
    private static UndoHandler undoHandler;
    private static int buttons;
    private static int buttons_before = 0;
    private static int redos;
    private HistoryChangeListener historyChangeListener = new HistoryChangeListener();
    private HistoryIMapViewChangeListener historyIMapViewChangeListener;

    public HistoryAction() {
        super("HistoryAction");
    }

    public void actionPerformed(final ActionEvent e) {
        if (dialog != null && dialog.isVisible())
            return;

        updateMap();

        panel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        panel.setLayout(gbl);
        scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        dialog = new JDialog(UITools.getCurrentFrame(), TextUtils.getText("HistoryAction.text"));

        update();

        dialog.add(scrollPane);
        dialog.setMinimumSize(new Dimension(181, 197));
        UITools.setBounds(dialog, -1, -1, 181, 197);
        dialog.setModal(false);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }

    private void updateMap() {
        final Controller controller = Controller.getCurrentController();
        final MapModel map = controller.getMap();
        if (undoHandler != null)
            undoHandler.removeChangeListener(historyChangeListener);
        if (map == null) {
            dialog.setVisible(false);
            return;
        }
        undoHandler = (UndoHandler) map.getExtension(IUndoHandler.class);
        if (undoHandler == null) {
            dialog.setVisible(false);
            return;
        }
        undoHandler.addChangeListener(historyChangeListener);
        if (historyIMapViewChangeListener == null) {
            historyIMapViewChangeListener = new HistoryIMapViewChangeListener();
            controller.getMapViewManager().addMapViewChangeListener(historyIMapViewChangeListener);
        }
        }

    private static void buttonPressed(int index) {
        int currentIndex = buttons - 1 - redos;
        if (index == currentIndex)
            return;
        if (index > currentIndex)
            for (int i = index - currentIndex; i > 0; i--)
                undoHandler.redo();
        if (index < currentIndex)
            for (int i = currentIndex - index; i > 0; i--)
                undoHandler.undo();
        update();
    }

    public static void setButtonUndo(JButton button) {
        button.setBorder(BorderFactory.createLoweredBevelBorder());
        button.setContentAreaFilled(false);
    }

    public static void update() {
        if (panel == null)
            return;
        if(undoHandler == null) {
            dialog.setVisible(false);
            return;
        }
        ListIterator<CompoundActor> actorIterator = undoHandler.getActorIterator();
        ListIterator<CompoundActor> actorIteratorUndo = undoHandler.getActorIteratorUndo();

        buttons = 0;
        redos = 0;
        panel.removeAll();
        dialog.repaint();

        GridBagLayout gbl = (GridBagLayout) panel.getLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;

        JButton lastButton = createButton("openFile", gbl, gbc, buttons++);
        panel.add(lastButton);
        while (actorIterator.hasNext()) {
            lastButton = createButton(actorIterator.next().getSimpleDescription(), gbl, gbc, buttons++);
            panel.add(lastButton);
        }
        gbc.weighty = 1;
        gbl.setConstraints(lastButton, gbc);

        //Mark undoed actions
        for (int i = buttons - 1; actorIteratorUndo.hasNext(); i--) {
            setButtonUndo((JButton) panel.getComponent(i));
            actorIteratorUndo.next();
            redos++;
        }
        dialog.validate();

        if(buttons_before != buttons) {
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        }
        buttons_before = buttons;
    }

    public static JButton createButton(String title, GridBagLayout gbl, GridBagConstraints gbc, int num) {
        if (title.isEmpty())
            return null;
        JButton button = new JButton(title);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        gbc.gridy = num;
        gbl.setConstraints(button, gbc);
        button.setMargin(new Insets(0, 0, 0, 0));
        final int buttonIndex = num;
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                buttonPressed(buttonIndex);
            }
        });
        return button;
    }

    private class HistoryChangeListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            update();
        }
    }

    private class HistoryIMapViewChangeListener implements IMapViewChangeListener {
        public void afterViewChange(Component oldView, Component newView) {
            updateMap();
            update();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        }

        public void afterViewClose(Component oldView) {
        }

        public void afterViewCreated(Component mapView) {
        }

        public void beforeViewChange(Component oldView, Component newView) {
        }
    }

}
