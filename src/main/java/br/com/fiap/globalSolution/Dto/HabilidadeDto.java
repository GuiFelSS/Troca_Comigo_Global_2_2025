package br.com.fiap.globalSolution.Dto;

import br.com.fiap.globalSolution.Enum.Category;
import br.com.fiap.globalSolution.Enum.Level;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class HabilidadeDto {

    @NotBlank
    private String name;

    @NotNull
    private Category category;

    @NotBlank
    private String description;

    @NotNull
    private Level level;

    @NotNull
    private Boolean isOffering;

    @NotNull
    private Boolean isSeeking;

    private Double hourlyRate;

    // --- Getters e Setters Manuais ---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Boolean getIsOffering() {
        return isOffering;
    }

    public void setIsOffering(Boolean offering) {
        isOffering = offering;
    }

    public Boolean getIsSeeking() {
        return isSeeking;
    }

    public void setIsSeeking(Boolean seeking) {
        isSeeking = seeking;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}