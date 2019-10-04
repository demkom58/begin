package net.minecraft.block;

final class StepSoundSand extends StepSound {
    StepSoundSand(String var1, float var2, float var3) {
        super(var1, var2, var3);
    }

    @Override
    public String stepSoundDir() {
        return "step.gravel";
    }
}
