public class Perso{
	
	private String = name;
	private int hp;
	private int hpmax;
	private int def;
	private int range;
	private int off;
	private int speed;
	private int magic;
	private int magicmax;
	private boolean alive;
	private int type;
	private int posX;
	private int posY;
	private int precis;
	private int joueur;
	
	public Perso(int h, int a ,int z,int e,int r,int t,int y,int u, int i, int o, int p, int m, int l, int k, int j){
		
		this.hp = a;
		this.hpmax = z;
		this.def = e;
		this.range = r;
		this.off = t;
		this.speed = y ;
		this.magic = u;
		this.magicmax = i;
		this.alive = o;
		this.type = p;
		this.li = m;
		this.co = l;
		this.precis = k;
		this.joueur = j;
		this.name = h;
		
	}
	
	public String getThis(){
		String resume = new String();
		resume = 
		return (this.name+" a encore "+this.hp+"pv, 
	
	public int getPosX(){
		return this.posX;
	}
	public int getPosY(){
		return this.posY;
	
	public int getDef();
		return this.def;
	}
	
	public void weaken(int pow){
		this.pv = this.pv - pow;
	}
	
	public void attac (Perso ennemy){
		
		int pow = this.off + this.precis*Math.random()*Math.pow(-1,(int)Math.random) - ennemy.getDef();
		
		ennemy.weaken(pow);
		
	}
		
		
		
