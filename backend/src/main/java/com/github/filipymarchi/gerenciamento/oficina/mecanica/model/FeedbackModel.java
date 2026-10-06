package com.github.filipymarchi.gerenciamento.oficina.mecanica.model;

import com.github.filipymarchi.gerenciamento.oficina.mecanica.model.ENUM.FeedbackStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "feedback")
public class FeedbackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column
    private FeedbackStatus feedbackStatus;

    @Column
    private String comentario;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revisao_id", nullable = false)
    private RevisaoModel revisao;
}
