package com.example.Park;
import jakarta.persistence.*;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq1")
    @SequenceGenerator(name="seq1",sequenceName = "seq",allocationSize = 1)
    int id;
}




