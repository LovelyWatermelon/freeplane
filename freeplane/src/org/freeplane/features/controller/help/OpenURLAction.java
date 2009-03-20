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
package org.freeplane.features.controller.help;

import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.freeplane.core.controller.Controller;
import org.freeplane.core.resources.FreeplaneResourceBundle;
import org.freeplane.core.resources.ResourceController;
import org.freeplane.core.ui.IFreeplaneAction;
import org.freeplane.core.ui.MenuBuilder;

class OpenURLAction extends AbstractAction implements IFreeplaneAction {
	private static final String NAME = "freeplaneUrl";
	private static final long serialVersionUID = 7266357451275984398L;
	final private Controller controller;
	final private String url;

	OpenURLAction(final Controller controller, final String description, final String url) {
		super(null, new ImageIcon(ResourceController.getResourceController().getResource("/images/Link.png")));
		MenuBuilder.setLabelAndMnemonic(this, description);
		this.url = url;
		this.controller = controller;
	}

	public void actionPerformed(final ActionEvent e) {
		try {
			controller.getViewController().openDocument(new URL(url));
		}
		catch (final MalformedURLException ex) {
			controller.errorMessage(FreeplaneResourceBundle.getText("url_error") + "\n" + ex);
		}
		catch (final Exception ex) {
			controller.errorMessage(ex);
		}
	}

	public String getName() {
		return NAME;
	}
}
