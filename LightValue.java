/**
 * This LightValue Enum lists the phases a stoplight lane may be in.
 * GREEN: Indicates that the right and middle lanes can proceed, but
 * the left lane cannot (for both directions).
 * LEFT_SIGNAL: Indicates that the left lane can proceed, but the
 * right and middle lanes cannot (for both directions).
 * RED indicates that no lane may proceed (for both directions).
 */
public enum LightValue {
    GREEN, RED, LEFT_SIGNAL
}
