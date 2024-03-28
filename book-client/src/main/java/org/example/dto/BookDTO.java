package org.example.dto;

import javax.swing.plaf.synth.SynthPopupMenuUI;
import java.util.UUID;

public record BookDTO(String title,String author,Integer quantity,String isbn) {
}
