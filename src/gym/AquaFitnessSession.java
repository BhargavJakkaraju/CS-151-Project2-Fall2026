package gym;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AquaFitnessSession extends FitnessSession {

    private static final double STANDARD_PRICE = 35.0;
    private static final double PREMIUM_PRICE = 20.0;

    private final String poolName;
    private final int capacity;
    private final List<Member> participants;

    public AquaFitnessSession(
            String sessionId,
            String trainerId,
            LocalDateTime startTime,
            int durationMinutes,
            String poolName,
            int capacity) {

        super(sessionId, trainerId, startTime, durationMinutes);

        if (poolName == null || poolName.trim().isEmpty()) {
            throw new IllegalArgumentException("Pool name cannot be blank.");
        }

        if (capacity < 1 || capacity > 100) {
            throw new IllegalArgumentException("Capacity must be between 1 and 100.");
        }

        this.poolName = poolName.trim();
        this.capacity = capacity;
        this.participants = new ArrayList<>();
    }

    @Override
    public double calculatePrice(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        if (!participants.contains(member)) {
            throw new IllegalStateException("Member is not enrolled in this aqua session.");
        }

        if (member.getMembershipType().equals("PREMIUM") && member.hasActiveMembership()) {
            return PREMIUM_PRICE;
        }

        return STANDARD_PRICE;
    }

    @Override
    public void addToClass(Member member) throws SessionFullException, MemberAlreadyInClassException {

        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        if (!member.hasActiveMembership()) {
            throw new IllegalStateException("An active, unfrozen membership is required.");
        }

        if (participants.contains(member)) {
            throw new MemberAlreadyInClassException();
        }

        if (participants.size() >= capacity) {
            throw new SessionFullException();
        }

        participants.add(member);
    }

    @Override
    public void removeMemberFromClass(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member cannot be null.");
        }

        if (!participants.remove(member)) {
            throw new IllegalStateException("Member is not enrolled in this aqua session.");
        }
    }

    public String getPoolName() {
        return poolName;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public boolean isEnrolled(Member member) {
        return participants.contains(member);
    }

    public List<Member> getParticipants() {
        return List.copyOf(participants);
    }

    @Override
    public String toString() {
        return super.toString()
                + ", Pool: " + poolName
                + ", Capacity: " + capacity
                + ", Participants: " + participants.size();
    }
}