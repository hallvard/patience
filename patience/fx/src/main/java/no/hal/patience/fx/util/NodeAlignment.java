package no.hal.patience.fx.util;

import javafx.geometry.Bounds;
import javafx.scene.Node;

public class NodeAlignment {
	
	private final Node relativeTo;
	private final float relativeAlignment;
	private final float alignment;
	private final double spacing;
	
	public NodeAlignment(Node relativeTo, float relativeAlignment, float alignment, double spacing) {
		this.relativeTo = relativeTo;
		this.relativeAlignment = relativeAlignment;
		this.alignment = alignment;
		this.spacing = spacing;
	}
	
	public static NodeAlignment at(Node relativeTo) { return new NodeAlignment(relativeTo, 0.0f, 0.0f, 0.0);}
	public static NodeAlignment centeredOn(Node relativeTo) { return new NodeAlignment(relativeTo, 0.5f, -0.5f, 0.0);}

	public static NodeAlignment above(Node relativeTo, double spacing) { return new NodeAlignment(relativeTo, 0.0f, -1.0f, spacing);}
	public static NodeAlignment leftOf(Node relativeTo, double spacing) { return new NodeAlignment(relativeTo, 0.0f, -1.0f, spacing);}
	
	public static NodeAlignment below(Node relativeTo, double spacing) { return new NodeAlignment(relativeTo, 1.0f, 0.0f, spacing);}
	public static NodeAlignment rightOf(Node relativeTo, double spacing) { return new NodeAlignment(relativeTo, 1.0f, 0.0f, spacing);}
	
	//

	public static void locate(Node node, NodeAlignment xAlignment, NodeAlignment yAlignment) {
		double x = xAlignment.relativeTo.getLayoutX() + xAlignment.relativeTo.getBoundsInLocal().getWidth() * xAlignment.relativeAlignment;
		double y = yAlignment.relativeTo.getLayoutY() + yAlignment.relativeTo.getBoundsInLocal().getHeight() * yAlignment.relativeAlignment;
		Bounds nodeBounds = node.getBoundsInLocal();
		double newX = x + nodeBounds.getWidth() * xAlignment.alignment + xAlignment.spacing;
		double newY = y + nodeBounds.getHeight() * yAlignment.alignment + yAlignment.spacing;
		if (node.getLayoutX() != newX || node.getLayoutY() != newY) {
			node.setLayoutX(newX);
			node.setLayoutY(newY);
		}
	}

	public static void xLocate(Node node, NodeAlignment xAlignment) {
		locate(node, xAlignment, at(node));
	}
	public static void yLocate(Node node, NodeAlignment yAlignment) {
		locate(node, at(node), yAlignment);
	}
}
