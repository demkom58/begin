package net.hypnosis.util.block;


import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import net.hypnosis.util.Collects;
import net.hypnosis.util.math.Direction;
import net.hypnosis.util.math.DirectionTransformation;

import static net.hypnosis.util.math.Direction.*;

public enum BlockRotation {
    NONE(DirectionTransformation.IDENTITY),
    CLOCKWISE_90(DirectionTransformation.ROT_90_Y_NEG),
    CLOCKWISE_180(DirectionTransformation.ROT_180_FACE_XZ),
    COUNTERCLOCKWISE_90(DirectionTransformation.ROT_90_Y_POS);

    private final DirectionTransformation directionTransformation;

    BlockRotation(DirectionTransformation directionTransformation) {
        this.directionTransformation = directionTransformation;
    }

    public BlockRotation rotate(BlockRotation rotation) {
        return switch (rotation) {
            case CLOCKWISE_180 -> switch (this) {
                case NONE -> CLOCKWISE_180;
                case CLOCKWISE_90 -> COUNTERCLOCKWISE_90;
                case CLOCKWISE_180 -> NONE;
                case COUNTERCLOCKWISE_90 -> CLOCKWISE_90;
            };
            case COUNTERCLOCKWISE_90 -> switch (this) {
                case NONE -> COUNTERCLOCKWISE_90;
                case CLOCKWISE_90 -> NONE;
                case CLOCKWISE_180 -> CLOCKWISE_90;
                case COUNTERCLOCKWISE_90 -> CLOCKWISE_180;
            };
            case CLOCKWISE_90 -> switch (this) {
                case NONE -> CLOCKWISE_90;
                case CLOCKWISE_90 -> CLOCKWISE_180;
                case CLOCKWISE_180 -> COUNTERCLOCKWISE_90;
                case COUNTERCLOCKWISE_90 -> NONE;
            };
            default -> this;
        };
    }

    public DirectionTransformation getDirectionTransformation() {
        return this.directionTransformation;
    }

    public Direction rotate(Direction direction) {
        if (direction.getAxis() == Axis.Y) {
            return direction;
        }

        return switch (this) {
            case CLOCKWISE_90 -> direction.rotateYClockwise();
            case CLOCKWISE_180 -> direction.getOpposite();
            case COUNTERCLOCKWISE_90 -> direction.rotateYCounterclockwise();
            default -> direction;
        };
    }

    public int rotate(int rotation, int fullTurn) {
        return switch (this) {
            case CLOCKWISE_90 -> (rotation + fullTurn / 4) % fullTurn;
            case CLOCKWISE_180 -> (rotation + fullTurn / 2) % fullTurn;
            case COUNTERCLOCKWISE_90 -> (rotation + fullTurn * 3 / 4) % fullTurn;
            default -> rotation;
        };
    }

    public static BlockRotation random(Random random) {
        return Collects.getRandom(values(), random);
    }

    public static List<BlockRotation> randomRotationOrder(Random random) {
        List<BlockRotation> list = Lists.newArrayList(values());
        Collections.shuffle(list, random);
        return list;
    }
}
