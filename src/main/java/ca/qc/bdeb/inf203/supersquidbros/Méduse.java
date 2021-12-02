package ca.qc.bdeb.inf203.supersquidbros;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Méduse extends GameObject {
    protected KeyCode left = KeyCode.LEFT, right = KeyCode.RIGHT, up = KeyCode.UP;
    protected Image imageSquid1Droite = new Image("meduse1.png");
    protected Image imageSquid1Gauche = new Image("meduse1-g.png");
    protected boolean vaADroite = true;
    protected boolean alreadyInTheAir = false;
    protected boolean enCollision = false;
    protected double hauteurPlateforme;
    protected boolean surPlateformeVerte = false;

    public Méduse() {
        this.vx = 0;
        this.vy = 0;
        this.ax = 0;
        this.ay = 1200;
        this.w = 50;
        this.h = 50;
        this.y = Main.HEIGHT - h;
        this.x = Main.WIDTH / 2;
    }

    public void setHauteurPlateforme(double hauteurPlateforme) {
        this.hauteurPlateforme = hauteurPlateforme;
    }

    public boolean isEnCollision() {
        return enCollision;
    }

    public void setEnCollision(boolean enCollision) {
        this.enCollision = enCollision;
    }

    public boolean isSurPlateformeVerte() {
        return surPlateformeVerte;
    }

    public void setSurPlateformeVerte(boolean surPlateformeVerte) {
        this.surPlateformeVerte = surPlateformeVerte;
    }

    @Override
    public void draw(GraphicsContext context, Camera camera) {
        verifieDirection();
        if (estEnModeDebug){
            context.setFill(Color.rgb(255, 0, 0, 0.4));
            context.fillRect(this.x, camera.calculerYCamera(this.y), 50,50);
        }
        double yAffichage = camera.calculerYCamera(y);
        if (vaADroite) {
            context.drawImage(imageSquid1Droite, x, yAffichage, w, h);
        }
        if (!vaADroite) {
            context.drawImage(imageSquid1Gauche, x, yAffichage, w, h);
        }
    }

    @Override
    public void update(double deltaTime) {
        boolean left = Input.isKeyPressed(this.left);
        boolean right = Input.isKeyPressed(this.right);
        boolean up = Input.isKeyPressed(this.up);

        if (left) {
            ax = -1200;
        } else if (right) {
            ax = 1200;
        } else {
            ax = 0;
            int signeVitesse = vx > 0 ? 1 : -1;
            double vitesseAmortissementX = -signeVitesse * 300;
            vx += deltaTime * vitesseAmortissementX;
            int nouveauSigneVitesse = vx > 0 ? 1 : -1;
            if (nouveauSigneVitesse != signeVitesse) {
                vx = 0;
            }
        }

        this.vx = vx + (ax * deltaTime);
        if (Math.abs(this.vx) > 175) {
            if (this.vx < 0) {
                this.vx = -175;
            }
            if (this.vx > 0) {
                this.vx = 175;
            }
        }


        double newX = x + (vx * deltaTime);
        if (newX > Main.WIDTH - w) {
            x = Main.WIDTH - w;
            this.vx = -vx;
        } else if (newX < 0) {
            x = 0;
            this.vx = -vx;
        } else
            x = newX;

        if (enCollision && !surPlateformeVerte) {
            vy = 0;
            y = hauteurPlateforme;
            alreadyInTheAir = false;
        } else {
            if (vy == 0) {
                alreadyInTheAir = false;
            } else if (vy != 0) {
                alreadyInTheAir = true;
            }
            vy = vy + (ay * deltaTime);
            double newY = y + (vy * deltaTime);
            if (newY > Main.HEIGHT - h) {
                vy = 0;
            } else {
                y = newY;
            }
        }
        if (up && !alreadyInTheAir) {
            vy = -600;
        }
    }

    private void verifieDirection() {
        if (this.vx > 0) {
            this.vaADroite = true;
        } else if (this.vx < 0) {
            this.vaADroite = false;
        }
    }

    public void setAlreadyInTheAir(boolean alreadyInTheAir) {
        this.alreadyInTheAir = alreadyInTheAir;
    }

}
