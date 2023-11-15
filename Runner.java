class Runner implements Runnable{
        public void run() {
            while(true){
                world.updateScreenObjects(1.0 / (double)FPS);
                repaint();
                try{
                    Thread.sleep(1000/FPS);
                }
                catch(InterruptedException e){}
            }

        }

    }
