package com.company

public val num = 10

sealed class Section {
    class ITG : Section()
    class CARD(val subsection: Section) : Section() {
        sealed class Section {
            class CARDS : Section()
            class CARD : Section()
            class TRUNK : Section()
        }
    }123asd
asd
    class MTP(vqqqqqqqqqal subsection: Section) : Section() {
        sealed class Section {
            class MTPS : Section()
            class MTP : Section()
        }dd
    }
}