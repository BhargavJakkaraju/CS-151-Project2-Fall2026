package gym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AquaFitnessSessionTest {

    private AquaFitnessSession session;
    private Member member;

    @BeforeEach
    void setUp() {
        session = new AquaFitnessSession(
                "AQUA101",
                "T001",
                LocalDateTime.now().plusDays(1),
                60,
                "Main Pool",
                2
        );

        member = new Member(
                "M001",
                "Test Member",
                "member@test.com"
        );

        member.purchaseMembership("BASIC", 1);
    }

    @Test
    void addMemberToAquaSession() throws Exception {
        session.addToClass(member);

        assertTrue(session.isEnrolled(member));
        assertEquals(1, session.getParticipantCount());
    }

    @Test
    void rejectDuplicateMember() throws Exception {
        session.addToClass(member);

        assertThrows(
                MemberAlreadyInClassException.class,
                () -> session.addToClass(member)
        );
    }

    @Test
    void rejectMemberWhenSessionIsFull() throws Exception {
        Member secondMember = new Member(
                "M002",
                "Second Member",
                "second@test.com"
        );

        Member thirdMember = new Member(
                "M003",
                "Third Member",
                "third@test.com"
        );

        secondMember.purchaseMembership("BASIC", 1);
        thirdMember.purchaseMembership("BASIC", 1);

        session.addToClass(member);
        session.addToClass(secondMember);

        assertThrows(
                SessionFullException.class,
                () -> session.addToClass(thirdMember)
        );
    }

    @Test
    void removeMemberFromAquaSession() throws Exception {
        session.addToClass(member);

        session.removeMemberFromClass(member);

        assertFalse(session.isEnrolled(member));
        assertEquals(0, session.getParticipantCount());
    }

    @Test
    void calculateStandardPrice() throws Exception {
        session.addToClass(member);

        assertEquals(35.0, session.calculatePrice(member));
    }

    @Test
    void calculatePremiumPrice() throws Exception {
        Member premiumMember = new Member(
                "M002",
                "Premium Member",
                "premium@test.com"
        );

        premiumMember.purchaseMembership("PREMIUM", 1);

        session.addToClass(premiumMember);

        assertEquals(20.0, session.calculatePrice(premiumMember));
    }

    @Test
    void rejectInactiveMember() {
        Member inactiveMember = new Member(
                "M002",
                "Inactive Member",
                "inactive@test.com"
        );

        assertThrows(
                IllegalStateException.class,
                () -> session.addToClass(inactiveMember)
        );
    }

    @Test
    void rejectInvalidCapacity() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AquaFitnessSession(
                        "AQUA102",
                        "T001",
                        LocalDateTime.now().plusDays(1),
                        60,
                        "Main Pool",
                        0
                )
        );
    }
}