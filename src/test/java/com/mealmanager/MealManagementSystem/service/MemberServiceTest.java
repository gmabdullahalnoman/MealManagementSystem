package com.mealmanager.MealManagementSystem.service;

import com.mealmanager.MealManagementSystem.entity.Member;
import com.mealmanager.MealManagementSystem.exception.ResourceNotFoundException;
import com.mealmanager.MealManagementSystem.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MemberService
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member("John Doe", "01712345678");
        testMember.setId(1L);
        testMember.setIsActive(true);
    }

    @Test
    void testCreateMember_Success() {
        // Arrange
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        Member result = memberService.createMember("John Doe", "01712345678");

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertTrue(result.getIsActive());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testGetMemberById_Found() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));

        // Act
        Optional<Member> result = memberService.getMemberById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(memberRepository, times(1)).findById(1L);
    }

    @Test
    void testGetMemberById_NotFound() {
        // Arrange
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Member> result = memberService.getMemberById(999L);

        // Assert
        assertFalse(result.isPresent());
        verify(memberRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateMember_Success() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        Member result = memberService.updateMember(1L, "Jane Doe", "01987654321");

        // Assert
        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember_NotFound() {
        // Arrange
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            memberService.updateMember(999L, "Jane Doe", "01987654321");
        });
        verify(memberRepository, times(1)).findById(999L);
        verify(memberRepository, never()).save(any());
    }

    @Test
    void testDeactivateMember_Success() {
        // Arrange
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        Member result = memberService.deactivateMember(1L);

        // Assert
        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testActivateMember_Success() {
        // Arrange
        testMember.setIsActive(false);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(testMember));
        when(memberRepository.save(any(Member.class))).thenReturn(testMember);

        // Act
        Member result = memberService.activateMember(1L);

        // Assert
        assertNotNull(result);
        verify(memberRepository, times(1)).findById(1L);
        verify(memberRepository, times(1)).save(any(Member.class));
    }
}
