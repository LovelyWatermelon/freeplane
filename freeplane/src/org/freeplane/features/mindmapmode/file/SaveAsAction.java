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
package org.freeplane.features.mindmapmode.file;

import java.awt.event.ActionEvent;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.IFreeplaneAction;
import org.freeplane.core.url.UrlManager;

class SaveAsAction extends AFreeplaneAction implements IFreeplaneAction {
	private static final String NAME = "saveAs";
	private static final long serialVersionUID = 2159332245929439677L;

	public SaveAsAction(final Controller controller) {
		super(controller, "save_as", "/images/filesaveas.png");
	}

	public void actionPerformed(final ActionEvent e) {
		((MFileManager) UrlManager.getController(getModeController())).saveAs(getController().getMap());
		getController().getViewController().setTitle();
	}

	public String getName() {
		return NAME;
	}
}
