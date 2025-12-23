package uk.ac.cam.mp2226.part1a.oop.proj1b;

public final class VoidV {
    public static final VoidV INST = new VoidV();

    private VoidV() {}
    public VoidV getInst() {
        return INST;
    }
}
