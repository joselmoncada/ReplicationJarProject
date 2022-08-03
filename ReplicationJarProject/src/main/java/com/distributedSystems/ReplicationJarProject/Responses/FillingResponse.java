package com.distributedSystems.ReplicationJarProject.Responses;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FillingResponse implements Serializable {
     int previous_A;
     int previous_B;
     int current_A;
     int current_B;

     public int getPrevious_A() {
          return previous_A;
     }

     public void setPrevious_A(int previous_A) {
          this.previous_A = previous_A;
     }

     public int getPrevious_B() {
          return previous_B;
     }

     public void setPrevious_B(int previous_B) {
          this.previous_B = previous_B;
     }

     public int getCurrent_A() {
          return current_A;
     }

     public void setCurrent_A(int current_A) {
          this.current_A = current_A;
     }

     public int getCurrent_B() {
          return current_B;
     }

     public void setCurrent_B(int current_B) {
          this.current_B = current_B;
     }

     public FillingResponse(int previous_A, int previous_B, int current_A, int current_B) {
          this.previous_A = previous_A;
          this.previous_B = previous_B;
          this.current_A = current_A;
          this.current_B = current_B;
     }
     @Override
     public String toString() {
          return "{ previous A: "+previous_A+"\nprevious B: "+previous_B+"\n current A: "+current_A+", \n current B: "+current_B+" }";
     }

}
