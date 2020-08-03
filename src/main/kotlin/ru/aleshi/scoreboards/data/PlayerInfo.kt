package ru.aleshi.scoreboards.data

/**
 * Information about player
 */
class PlayerInfo {
    /**
     * Player score points
     */
    var score: Int = 0

    /**
     * Count of A-type warnings
     */
    var aWarnings: Int = 0

    /**
     * Count of D-type warnings
     */
    var dWarnings: Int = 0

    /**
     * Count of M-type warnings
     */
    var mWarnings: Int = 0

    /**
     * Resets all parameters to zero.
     */
    fun reset() {
        score = 0
        aWarnings = 0
        dWarnings = 0
        mWarnings = 0
    }
}