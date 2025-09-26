
interface Observer {
    void update(int temp);
}
class Weather {
    private Observer obs; 
    public void addObserver(Observer o) { obs = o; }
    public void setTemp(int t) {
        if (obs != null) obs.update(t);
    }
}
class Display implements Observer {
    public void update(int temp) {
        System.out.println("Temperature: " + temp);
    }
}

interface Greeting { void sayHello(); }
class EnglishGreeting implements Greeting {
    public void sayHello() { System.out.println("Hello!"); }
}
class HindiGreeting implements Greeting {
    public void sayHello() { System.out.println("Namaste!"); }
}
class Greeter {
    Greeting g;
    Greeter(Greeting g) { this.g = g; }
    void greet() { g.sayHello(); }
}

class Printer {
    private static Printer instance = new Printer();
    private Printer() {}
    public static Printer get() { return instance; }
    public void print(String msg) { System.out.println("Printing: " + msg); }
}


interface Shape { void draw(); }
class Circle implements Shape {
    public void draw() { System.out.println("Drawing Circle"); }
}
class Square implements Shape {
    public void draw() { System.out.println("Drawing Square"); }
}
class ShapeFactory {
    Shape getShape(String type) {
        if (type.equals("circle")) return new Circle();
        if (type.equals("square")) return new Square();
        return null;
    }
}

interface Media { void play(String name); }
class AudioPlayer implements Media {
    public void play(String name) { System.out.println("Playing audio: " + name); }
}
class VideoPlayer {
    void playVideo(String name) { System.out.println("Playing video: " + name); }
}
class VideoAdapter implements Media {
    VideoPlayer vp = new VideoPlayer();
    public void play(String name) { vp.playVideo(name); }
}


interface Coffee { String make(); }
class BasicCoffee implements Coffee {
    public String make() { return "Coffee"; }
}
class Milk implements Coffee {
    Coffee c;
    Milk(Coffee c) { this.c = c; }
    public String make() { return c.make() + " + Milk"; }
}
class Sugar implements Coffee {
    Coffee c;
    Sugar(Coffee c) { this.c = c; }
    public String make() { return c.make() + " + Sugar"; }
}


public class SoftwarePattterns{
    public static void main(String[] args) {
        System.out.println("Observer Pattern");
        Weather w = new Weather();
        w.addObserver(new Display());
        w.setTemp(28);

        System.out.println("\nStrategy Pattern");
        new Greeter(new EnglishGreeting()).greet();
        new Greeter(new HindiGreeting()).greet();

        System.out.println("\n Singleton Pattern");
        Printer.get().print("My Document");

        System.out.println("\nFactory Pattern");
        ShapeFactory f = new ShapeFactory();
        f.getShape("circle").draw();
        f.getShape("square").draw();

        System.out.println("\nAdapter Pattern");
        Media m1 = new AudioPlayer();
        m1.play("song.mp3");
        Media m2 = new VideoAdapter();
        m2.play("movie.mp4");

        System.out.println("\nDecorator Pattern");
        Coffee c = new Sugar(new Milk(new BasicCoffee()));
        System.out.println(c.make());
    }
}
