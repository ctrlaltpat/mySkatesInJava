package com.ctrlaltpat.MySkates.service;

import com.ctrlaltpat.MySkates.exception.ResourceNotFoundException;
import com.ctrlaltpat.MySkates.model.RollerSkatesPair;
import com.ctrlaltpat.MySkates.repository.RollerSkatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RollerSkatesServiceTest {

    @Mock
    private RollerSkatesRepository rollerSkatesRepository;

    @InjectMocks
    private RollerSkatesService rollerSkatesService;

    private RollerSkatesPair testSkate;

    @BeforeEach
    void setUp() {
        testSkate = new RollerSkatesPair("OG 172", "Riedell", "Chaya Ophira", "RollerBones Teams (white)");
        testSkate.setId(1L);
    }

    @Test
    void getAllRollerSkates_ShouldReturnAllSkates() {
        RollerSkatesPair skate2 = new RollerSkatesPair("3200", "Riedell", "Arius", "Varsity Plus (black)");
        skate2.setId(2L);
        List<RollerSkatesPair> expectedSkates = Arrays.asList(testSkate, skate2);
        when(rollerSkatesRepository.findAll()).thenReturn(expectedSkates);

        List<RollerSkatesPair> actualSkates = rollerSkatesService.getAllRollerSkates();

        assertThat(actualSkates).hasSize(2);
        assertThat(actualSkates).containsExactly(testSkate, skate2);
        verify(rollerSkatesRepository).findAll();
    }

    @Test
    void getAllRollerSkates_WhenNoSkates_ShouldReturnEmptyList() {
        when(rollerSkatesRepository.findAll()).thenReturn(Arrays.asList());

        List<RollerSkatesPair> actualSkates = rollerSkatesService.getAllRollerSkates();

        assertThat(actualSkates).isEmpty();
        verify(rollerSkatesRepository).findAll();
    }

    @Test
    void getRollerSkatesById_WhenSkateExists_ShouldReturnSkate() {
        when(rollerSkatesRepository.findById(1L)).thenReturn(Optional.of(testSkate));

        Optional<RollerSkatesPair> result = rollerSkatesService.getRollerSkatesById(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testSkate);
        verify(rollerSkatesRepository).findById(1L);
    }

    @Test
    void getRollerSkatesById_WhenSkateDoesNotExist_ShouldReturnEmpty() {
        when(rollerSkatesRepository.findById(336L)).thenReturn(Optional.empty());

        Optional<RollerSkatesPair> result = rollerSkatesService.getRollerSkatesById(336L);

        assertThat(result).isEmpty();
        verify(rollerSkatesRepository).findById(336L);
    }

    @Test
    void createRollerSkates_ShouldSaveAndReturnSkate() {
        RollerSkatesPair inputSkate = new RollerSkatesPair("Test", "Test Brand", "Test Plates", "Test Wheels");
        RollerSkatesPair savedSkate = new RollerSkatesPair("Test", "Test Brand", "Test Plates", "Test Wheels");
        savedSkate.setId(1L);
        when(rollerSkatesRepository.save(inputSkate)).thenReturn(savedSkate);

        RollerSkatesPair result = rollerSkatesService.createRollerSkates(inputSkate);

        assertThat(result).isEqualTo(savedSkate);
        assertThat(result.getId()).isEqualTo(1L);
        verify(rollerSkatesRepository).save(inputSkate);
    }

    @Test
    void createRollerSkates_WithNullInput_ShouldPassToRepository() {
        when(rollerSkatesRepository.save(null)).thenThrow(new IllegalArgumentException("Entity must not be null"));

        assertThatThrownBy(() -> rollerSkatesService.createRollerSkates(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Entity must not be null");
        verify(rollerSkatesRepository).save(null);
    }

    @Test
    void updateRollerSkates_WhenSkateExists_ShouldUpdateAndReturnSkate() {
        RollerSkatesPair updateData = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                "Updated Wheels");
        RollerSkatesPair updatedSkate = new RollerSkatesPair("Updated Name", "Updated Brand", "Updated Plates",
                "Updated Wheels");
        updatedSkate.setId(1L);

        when(rollerSkatesRepository.findById(1L)).thenReturn(Optional.of(testSkate));
        when(rollerSkatesRepository.save(any(RollerSkatesPair.class))).thenReturn(updatedSkate);

        RollerSkatesPair result = rollerSkatesService.updateRollerSkates(1L, updateData);

        assertThat(result.getName()).isEqualTo("Updated Name");
        assertThat(result.getBrand()).isEqualTo("Updated Brand");
        assertThat(result.getPlates()).isEqualTo("Updated Plates");
        assertThat(result.getWheels()).isEqualTo("Updated Wheels");
        assertThat(result.getId()).isEqualTo(1L);

        verify(rollerSkatesRepository).findById(1L);
        verify(rollerSkatesRepository).save(any(RollerSkatesPair.class));
    }

    @Test
    void updateRollerSkates_WhenSkateDoesNotExist_ShouldThrowException() {
        RollerSkatesPair updateData = new RollerSkatesPair("Updated", "Updated", "Updated", "Updated");
        when(rollerSkatesRepository.findById(336L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rollerSkatesService.updateRollerSkates(336L, updateData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Roller Skates not found");

        verify(rollerSkatesRepository).findById(336L);
        verify(rollerSkatesRepository, never()).save(any());
    }

    @Test
    void updateRollerSkates_WithPartialData_ShouldUpdateOnlyProvidedFields() {

        RollerSkatesPair updateData = new RollerSkatesPair("New Name", null, null, "New Wheels");
        when(rollerSkatesRepository.findById(1L)).thenReturn(Optional.of(testSkate));
        when(rollerSkatesRepository.save(any(RollerSkatesPair.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RollerSkatesPair result = rollerSkatesService.updateRollerSkates(1L, updateData);

        assertThat(result.getName()).isEqualTo("New Name");
        assertThat(result.getBrand()).isNull();
        assertThat(result.getPlates()).isNull();
        assertThat(result.getWheels()).isEqualTo("New Wheels");

        verify(rollerSkatesRepository).findById(1L);
        verify(rollerSkatesRepository).save(testSkate);
    }

    @Test
    void deleteRollerSkates_WhenSkateExists_ShouldDeleteSkate() {
        when(rollerSkatesRepository.findById(1L)).thenReturn(Optional.of(testSkate));
        doNothing().when(rollerSkatesRepository).delete(testSkate);

        rollerSkatesService.deleteRollerSkates(1L);

        verify(rollerSkatesRepository).findById(1L);
        verify(rollerSkatesRepository).delete(testSkate);
    }

    @Test
    void deleteRollerSkates_WhenSkateDoesNotExist_ShouldThrowException() {
        when(rollerSkatesRepository.findById(336L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rollerSkatesService.deleteRollerSkates(336L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Roller Skates not found");

        verify(rollerSkatesRepository).findById(336L);
        verify(rollerSkatesRepository, never()).delete(any());
    }

    @Test
    void deleteRollerSkates_WhenRepositoryThrowsException_ShouldPropagateException() {
        when(rollerSkatesRepository.findById(1L)).thenReturn(Optional.of(testSkate));
        doThrow(new RuntimeException("Database error")).when(rollerSkatesRepository).delete(testSkate);

        assertThatThrownBy(() -> rollerSkatesService.deleteRollerSkates(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");

        verify(rollerSkatesRepository).findById(1L);
        verify(rollerSkatesRepository).delete(testSkate);
    }
}