/*
 *  Freeplane - mind map editor
 *  Copyright (C) 2009 Dimitry Polivaev
 *
 *  This file author is Dimitry Polivaev
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
package org.freeplane.features.mindmapmode.link;

import java.awt.event.ActionEvent;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.model.NodeModel;
import org.freeplane.core.ui.MultipleNodeAction;
import org.freeplane.features.common.link.LinkController;

/**
 * @author Dimitry Polivaev
 * Mar 20, 2009
 */
public class ExtractLinkFromTextAction extends MultipleNodeAction {
	public ExtractLinkFromTextAction(Controller controller) {
	    super(controller, "extract_link_from_text");
    }

	/**
     * 
     */
    private static final long serialVersionUID = -2579214095445662717L;

	@Override
	protected void actionPerformed(ActionEvent e, NodeModel node) {
		final MLinkController controller = (MLinkController) LinkController.getController(getModeController());
		String link = controller.findLink(node.getText());
		if(link != null){
			controller.setLink(node, link);
		}
	}
}
