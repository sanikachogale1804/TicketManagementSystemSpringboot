package com.example.Demo.TicketManagementSystemCogent_1.Service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Demo.TicketManagementSystemCogent_1.Entity.Ticket;
import com.example.Demo.TicketManagementSystemCogent_1.Entity.User;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.TicketRepository;
import com.example.Demo.TicketManagementSystemCogent_1.Repository.UserRepository;

@Service
public class AutoAssignmentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private EmailService emailService;

    public User getBestTeamMember() {
        List<User> teamMembers = userRepository.findByRole(User.Role.TEAMMEMBER);
        if (teamMembers.isEmpty()) {
            throw new RuntimeException("No team members available for auto assignment");
        }

        return teamMembers.stream()
                .map(member -> {
                    long active = ticketRepository.countActiveTicketsByUser(member);  // OPEN + IN_PROGRESS
                    long closed = ticketRepository.countClosedTicketsByUser(member);  
                    return new MemberLoad(member, active, closed);
                })
                .sorted(
                    Comparator
                        .comparingLong(MemberLoad::getActiveTickets)                
                        .thenComparing(Comparator.comparingLong(MemberLoad::getClosedTickets).reversed()) 
                )
                .map(MemberLoad::getMember)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No eligible team member found"));
    }

    public Ticket autoAssign(Ticket ticket) {

        User bestMember = getBestTeamMember();
        ticket.setAssignedTo(bestMember);
        ticket.setStatus(Ticket.Status.IN_PROGRESS);

        Ticket savedTicket = ticketRepository.save(ticket);

        emailService.sendTicketAssignedMail(savedTicket, bestMember);

        return savedTicket;
    }


    private static class MemberLoad {
        private final User member;
        private final long activeTickets;
        private final long closedTickets;

        public MemberLoad(User member, long activeTickets, long closedTickets) {
            this.member = member;
            this.activeTickets = activeTickets;
            this.closedTickets = closedTickets;
        }

        public User getMember() { return member; }
        public long getActiveTickets() { return activeTickets; }
        public long getClosedTickets() { return closedTickets; }
    }
}