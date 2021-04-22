package com.fspt.safedrive.bean;

import java.util.List;

/**
 * Create by Spring on 2021/4/13 16:58
 */

public class Person {


    /**
     * log_id : 1381894227218137088
     * person_num : 1
     * person_info : [{"attributes":{"both_hands_leaving_wheel":{"score":0.277991771698,"threshold":0.75},"eyes_closed":{"score":0.27688577398658,"threshold":0.55},"no_face_mask":{"score":0.98868244886398,"threshold":0.75},"not_buckling_up":{"score":0.0030267152469605,"threshold":0.44},"smoke":{"score":0.002467458602041,"threshold":0.48},"cellphone":{"score":0.999999,"threshold":0.69},"not_facing_front":{"score":0.70175904035568,"threshold":0.5},"yawning":{"score":0.0024807156878524,"threshold":0.5},"head_lowered":{"score":0.51333971321583,"threshold":0.55}},"location":{"score":0.96831691265106,"top":5,"left":12,"width":486,"height":325}}]
     * driver_num : 1
     */

    private Long log_id;
    private Integer person_num;
    private Integer driver_num;
    private List<PersonInfoDTO> person_info;


    public Integer getPerson_num() {
        return person_num;
    }

    public Integer getDriver_num() {
        return driver_num;
    }

    public List<PersonInfoDTO> getPerson_info() {
        return person_info;
    }

    public static class PersonInfoDTO {
        public AttributesDTO getAttributes() {
            return attributes;
        }

        /**
         * attributes : {"both_hands_leaving_wheel":{"score":0.277991771698,"threshold":0.75},"eyes_closed":{"score":0.27688577398658,"threshold":0.55},"no_face_mask":{"score":0.98868244886398,"threshold":0.75},"not_buckling_up":{"score":0.0030267152469605,"threshold":0.44},"smoke":{"score":0.002467458602041,"threshold":0.48},"cellphone":{"score":0.999999,"threshold":0.69},"not_facing_front":{"score":0.70175904035568,"threshold":0.5},"yawning":{"score":0.0024807156878524,"threshold":0.5},"head_lowered":{"score":0.51333971321583,"threshold":0.55}}
         * location : {"score":0.96831691265106,"top":5,"left":12,"width":486,"height":325}
         */

        private AttributesDTO attributes;
        private LocationDTO location;


        public static class AttributesDTO {
            public BothHandsLeavingWheelDTO getBoth_hands_leaving_wheel() {
                return both_hands_leaving_wheel;
            }

            public EyesClosedDTO getEyes_closed() {
                return eyes_closed;
            }

            public NoFaceMaskDTO getNo_face_mask() {
                return no_face_mask;
            }

            public NotBucklingUpDTO getNot_buckling_up() {
                return not_buckling_up;
            }

            public SmokeDTO getSmoke() {
                return smoke;
            }

            public CellphoneDTO getCellphone() {
                return cellphone;
            }

            public NotFacingFrontDTO getNot_facing_front() {
                return not_facing_front;
            }

            public YawningDTO getYawning() {
                return yawning;
            }

            public HeadLoweredDTO getHead_lowered() {
                return head_lowered;
            }

            /**
             * both_hands_leaving_wheel : {"score":0.277991771698,"threshold":0.75}
             * eyes_closed : {"score":0.27688577398658,"threshold":0.55}
             * no_face_mask : {"score":0.98868244886398,"threshold":0.75}
             * not_buckling_up : {"score":0.0030267152469605,"threshold":0.44}
             * smoke : {"score":0.002467458602041,"threshold":0.48}
             * cellphone : {"score":0.999999,"threshold":0.69}
             * not_facing_front : {"score":0.70175904035568,"threshold":0.5}
             * yawning : {"score":0.0024807156878524,"threshold":0.5}
             * head_lowered : {"score":0.51333971321583,"threshold":0.55}
             */

            private BothHandsLeavingWheelDTO both_hands_leaving_wheel;
            private EyesClosedDTO eyes_closed;
            private NoFaceMaskDTO no_face_mask;
            private NotBucklingUpDTO not_buckling_up;
            private SmokeDTO smoke;
            private CellphoneDTO cellphone;
            private NotFacingFrontDTO not_facing_front;
            private YawningDTO yawning;
            private HeadLoweredDTO head_lowered;


            public static class BothHandsLeavingWheelDTO {
                /**
                 * score : 0.277991771698
                 * threshold : 0.75
                 */

                private Double score;
                private Double threshold;

                public Double getScore() {
                    return score;
                }

                public void setScore(Double score) {
                    this.score = score;
                }

                public Double getThreshold() {
                    return threshold;
                }

                public void setThreshold(Double threshold) {
                    this.threshold = threshold;
                }
            }

            public static class EyesClosedDTO {
                public Double getScore() {
                    return score;
                }

                /**
                 * score : 0.27688577398658
                 * threshold : 0.55
                 */

                private Double score;
                private Double threshold;
            }


            public static class NoFaceMaskDTO {
                public Double getScore() {
                    return score;
                }

                /**
                 * score : 0.98868244886398
                 * threshold : 0.75
                 */

                private Double score;
                private Double threshold;
            }


            public static class NotBucklingUpDTO {
                /**
                 * score : 0.0030267152469605
                 * threshold : 0.44
                 */

                private Double score;
                private Double threshold;
            }


            public static class SmokeDTO {
                public Double getScore() {
                    return score;
                }

                /**
                 * score : 0.002467458602041
                 * threshold : 0.48
                 */

                private Double score;
                private Double threshold;
            }


            public static class CellphoneDTO {
                public Double getScore() {
                    return score;
                }

                /**
                 * score : 0.999999
                 * threshold : 0.69
                 */

                private Double score;
                private Double threshold;
            }


            public static class NotFacingFrontDTO {
                /**
                 * score : 0.70175904035568
                 * threshold : 0.5
                 */

                private Double score;
                private Double threshold;
            }

            public static class YawningDTO {
                public Double getScore() {
                    return score;
                }

                /**
                 * score : 0.0024807156878524
                 * threshold : 0.5
                 */

                private Double score;
                private Double threshold;
            }


            public static class HeadLoweredDTO {
                /**
                 * score : 0.51333971321583
                 * threshold : 0.55
                 */

                private Double score;
                private Double threshold;
            }
        }


        public static class LocationDTO {
            /**
             * score : 0.96831691265106
             * top : 5
             * left : 12
             * width : 486
             * height : 325
             */

            private Double score;
            private Integer top;
            private Integer left;
            private Integer width;
            private Integer height;
        }
    }
}
