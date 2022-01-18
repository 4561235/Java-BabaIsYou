package babaIsYou.model;
import java.util.Objects;

@SuppressWarnings("preview")
public record Point(int x, int y) {

	public Point{
		Objects.requireNonNull(x);
		Objects.requireNonNull(y);
	}
	
	public Point add(Point point) {
		return new Point(this.x + point.x(), this.y + point.y());
	}
	
	public Point sub(Point point) {
		return new Point(this.x - point.x(),this.y - point.y() );
	}
	
	public Point clone() {
		return new Point(this.x,this.y);
	}
}

