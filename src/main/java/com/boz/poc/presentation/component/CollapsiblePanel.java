package com.boz.poc.presentation.component;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@FacesComponent("com.boz.poc.presentation.component.CollapsiblePanel")
public class CollapsiblePanel extends UINamingContainer {

	enum PropertyKeys {
		collapsed
	}

	public boolean isCollapsed() {
		return (Boolean) getStateHelper().eval(PropertyKeys.collapsed, Boolean.TRUE);
	}

	public void setCollapsed(final boolean collapsed) {
		getStateHelper().put(PropertyKeys.collapsed, collapsed);
	}

	public void toggle(final ActionEvent e) {
		setCollapsed(!isCollapsed());
		setCollapsedValueExpression();
	}

	private void setCollapsedValueExpression() {
		final ELContext ctx = FacesContext.getCurrentInstance().getELContext();
		final ValueExpression ve = getValueExpression(PropertyKeys.collapsed.name());
		if (ve != null) {
			ve.setValue(ctx, isCollapsed());
		}
	}

}