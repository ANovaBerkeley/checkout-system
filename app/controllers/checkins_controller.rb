class CheckinsController < ApplicationController
  before_action :set_checkin, only: [:show, :edit, :update, :destroy]

  # GET /checkins
  # GET /checkins.json
  def index
    @checkins = Checkin.all
  end

  # GET /checkins/1
  # GET /checkins/1.json
  def show
  end

  # GET /checkins/new
  def new
    @checkin = Checkin.new
  end

  # GET /checkins/1/edit
  def edit
  end

  # POST /checkins
  # POST /checkins.json
  def create
    @checkin = Checkin.new(checkin_params)

    respond_to do |format|
      if @checkin.save
        format.html { redirect_to @checkin, notice: 'Checkin was successfully created.' }
        format.json { render :show, status: :created, location: @checkin }
      else
        format.html { render :new }
        format.json { render json: @checkin.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /checkins/1
  # PATCH/PUT /checkins/1.json
  def update
    # respond_to do |format|
    #   if @checkin.update(checkin_params)
    #     format.html { redirect_to @checkin, notice: 'Checkin was successfully updated.' }
    #     format.json { render :show, status: :ok, location: @checkin }
    #   else
    #     format.html { render :edit }
    #     format.json { render json: @checkin.errors, status: :unprocessable_entity }
    #   end
    # end
  end

  # DELETE /checkins/1
  # DELETE /checkins/1.json
  def destroy
    # @checkin.destroy
    # respond_to do |format|
    #   format.html { redirect_to checkins_url, notice: 'Checkin was successfully destroyed.' }
    #   format.json { head :no_content }
    # end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_checkin
      @checkin = Checkin.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def checkin_params
      params.require(:checkin).permit(:is_mentor)
    end
end
